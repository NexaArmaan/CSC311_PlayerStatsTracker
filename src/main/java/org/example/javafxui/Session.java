package org.example.javafxui;

import org.example.javafxui.model.User;
import service.GameService;

public class Session {
    public static User currentUser;
    public static GameService gameService = new GameService();
}