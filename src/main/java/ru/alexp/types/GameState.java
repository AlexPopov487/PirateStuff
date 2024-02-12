package ru.alexp.types;

public enum GameState {
    PLAYING,
    MENU,
    OPTIONS,
    QUIT,
    GAME_OVER;

    private static GameState state = MENU;

    public static GameState getState() {
        return state;
    }

    public static void setState(GameState state) {
        GameState.state = state;
    }
}
