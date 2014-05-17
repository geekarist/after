package com.github.geekarist.after;

import org.junit.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AfterAppTest {

    private int delay;
    private int initialTrayIcons;

    @Test
    public void should_show_tray_icon() {
        given_delay(10);
        given_initial_tray_icons();
        when_launching_app();
        then_tray_icon_shows_up();
    }

    private void given_initial_tray_icons() {
        initialTrayIcons = SystemTray.getSystemTray().getTrayIcons().length;
    }

    private void then_tray_icon_shows_up() {
        TrayIcon[] icons = SystemTray.getSystemTray().getTrayIcons();
        assertThat(icons.length).isEqualTo(initialTrayIcons + 1);
        assertThat(icons[0].getActionCommand()).isEqualTo("After");
    }

    private void when_launching_app() {
        AfterApp app = new AfterApp(delay);
        app.launch();
    }

    private void given_delay(int delay) {
        this.delay = delay;
    }

}
