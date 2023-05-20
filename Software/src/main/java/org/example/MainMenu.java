package org.example;

import javax.accessibility.Accessible;
import java.awt.*;
import java.io.Serializable;

public interface MainMenu extends Serializable, Accessible, MenuContainer {
    // implementation of the Menu interface
    void login();

    void logout();
}
