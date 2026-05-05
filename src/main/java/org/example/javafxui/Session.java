package org.example.javafxui;

import org.example.javafxui.db.ConnDbOps;
import org.example.javafxui.model.User;
import org.example.javafxui.service.GameService;

public class Session {
    public static User currentUser;
    public static ConnDbOps db = new ConnDbOps();
    public static GameService gameService = new GameService(db);
}