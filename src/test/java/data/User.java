package data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum User {

    STANDARD("standard_user", "secret_sauce"),
    LOCKED_OUT("locked_out_user", "secret_sauce"),
    PROBLEM("problem_user", "secret_sauce"),
    PERFORMANCE_GLITCH("performance_glitch_user", "secret_sauce");

    @Getter
    private final String username;

    @Getter
    private final String password;

}
