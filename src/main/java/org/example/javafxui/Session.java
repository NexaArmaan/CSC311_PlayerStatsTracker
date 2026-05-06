package org.example.javafxui;

import org.example.javafxui.db.ConnDbOps;
import org.example.javafxui.db.repositories.AnalyticsRepository;
import org.example.javafxui.db.repositories.GameRepository;
import org.example.javafxui.db.repositories.StatsRepository;
import org.example.javafxui.db.repositories.UserRepository;
import org.example.javafxui.model.User;
import org.example.javafxui.service.GameService;

public class Session {
    public static User currentUser;
    public static ConnDbOps db = new ConnDbOps();
    public static AnalyticsRepository analytics = new AnalyticsRepository(db.getConnection());
    public static GameRepository game = new GameRepository(db.getConnection());
    public static UserRepository user = new UserRepository(db.getConnection());
    public static StatsRepository stats = new StatsRepository(db.getConnection());
    public static GameService gameService = new GameService(db);
}