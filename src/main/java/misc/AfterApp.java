/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package misc;
/*
 * AfterApp.java
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import javax.swing.*;
import org.capaxit.imagegenerator.impl.TextImageImpl;

public class AfterApp {

    public static void main(String[] args) {
        setupUI();

        final int timeToAlarm = Integer.parseInt(args[0]) * 1000;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowComponents(timeToAlarm);
            }
        });
    }

    private static void setupUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
    }

    private static void createAndShowComponents(int timeToAlarm) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        final PopupMenu popup = new PopupMenu();
        popup.add(about());

        final SystemTray tray = SystemTray.getSystemTray();
        final TrayIcon trayIcon = trayIcon(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        popup.add(exitItem(trayIcon));

        final long targetTime = Calendar.getInstance().getTimeInMillis() + timeToAlarm;

        timesUpTimer(timeToAlarm, trayIcon).start();
        trayUpdateTimer(targetTime, trayIcon).start();
    }

    private static Timer trayUpdateTimer(final long targetTime, final TrayIcon trayIcon) {
        return new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent thisTimer) {
                    long remaining = targetTime - thisTimer.getWhen();
                    if (remaining >= 0) {
                        trayIcon.setImage(timeIcon("" + (remaining / 1000)).getBufferedImage());
                    }
                }
            });
    }

    private static Timer timesUpTimer(int timeMillis, final TrayIcon trayIcon) {
        return new Timer(timeMillis, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent thisTimer) {
                    trayIcon.setImage(timeIcon("0").getBufferedImage());
                    JOptionPane.showMessageDialog(null, "Time's up!");
                    ((Timer) thisTimer.getSource()).stop();
                    SystemTray.getSystemTray().remove(trayIcon);
                    System.exit(0);
                }
            });
    }

    private static MenuItem exitItem(final TrayIcon trayIcon) {
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SystemTray.getSystemTray().remove(trayIcon);
                System.exit(0);
            }
        });
        return exitItem;
    }

    private static TrayIcon trayIcon(PopupMenu popup) {
        TextImageImpl textImage = timeIcon("--");
        final TrayIcon trayIcon
                = new TrayIcon((new ImageIcon(textImage.getBufferedImage(), "tray icon")).getImage());
        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);
        return trayIcon;
    }

    private static MenuItem about() {
        MenuItem about = new MenuItem("About");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from the About menu item");
            }
        });
        return about;
    }

    private static TextImageImpl timeIcon(final String time) {
        TextImageImpl textImage = new TextImageImpl(56, 56);
        textImage.withFont(new Font("Sans-Serif", Font.BOLD, 38)).write(time);
        return textImage;
    }
}
