package com.github.geekarist.after;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import javax.swing.*;
import org.capaxit.imagegenerator.impl.TextImageImpl;

public class AfterApp {

    public static void main(String[] args) {
        final int timeToAlarm = Integer.parseInt(args[0]) * 1000;
        new AfterApp(timeToAlarm).launch();
    }

    private final int timeToAlarm;
    private Timer timesUpTimer;
    private Timer trayUpdateTimer;

    private AfterApp(int timeToAlarm) {
        this.timeToAlarm = timeToAlarm;
    }

    public void launch() {
        setup();
        init();
        start();
    }

    private void setup() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            System.exit(1);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
    }


    private void init() {
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = trayIcon(popup);
        popup.add(aboutItem());
        popup.add(exitItem(trayIcon));
        this.timesUpTimer = timesUpTimer(timeToAlarm, trayIcon);
        this.trayUpdateTimer = trayUpdateTimer(timeToAlarm, trayIcon);
        addIconToTray(trayIcon);
    }

    private void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                trayUpdateTimer.start();
                timesUpTimer.start();
            }
        });
    }

    private void addIconToTray(final TrayIcon trayIcon) {
        final SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    private MenuItem aboutItem() {
        MenuItem about = new MenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
            }
        });
        return about;
    }

    private MenuItem exitItem(final TrayIcon trayIcon) {
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemTray.getSystemTray().remove(trayIcon);
                System.exit(0);
            }
        });
        return exitItem;
    }

    private TrayIcon trayIcon(PopupMenu popup) {
        TextImageImpl textImage = timeImage("--");
        final TrayIcon trayIcon
                = new TrayIcon((new ImageIcon(textImage.getBufferedImage(), "tray icon")).getImage());
        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);
        return trayIcon;
    }

    private TextImageImpl timeImage(final String time) {
        TextImageImpl textImage = new TextImageImpl(56, 56);
        textImage.withFont(new Font("Sans-Serif", Font.BOLD, 38)).write(time);
        return textImage;
    }

    private Timer timesUpTimer(int timeMillis, final TrayIcon trayIcon) {
        return new Timer(timeMillis, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent thisTimer) {
                trayIcon.setImage(timeImage("0").getBufferedImage());
                JOptionPane.showMessageDialog(null, "Time's up!");
                ((Timer) thisTimer.getSource()).stop();
                SystemTray.getSystemTray().remove(trayIcon);
                System.exit(0);
            }
        });
    }

    private Timer trayUpdateTimer(final long timeToAlarm, final TrayIcon trayIcon) {
        final long targetTime = Calendar.getInstance().getTimeInMillis() + timeToAlarm;
        return new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent thisTimer) {
                long remaining = targetTime - thisTimer.getWhen();
                if (remaining >= 0) {
                    trayIcon.setImage(timeImage("" + (remaining / 1000)).getBufferedImage());
                }
            }
        });
    }
}
