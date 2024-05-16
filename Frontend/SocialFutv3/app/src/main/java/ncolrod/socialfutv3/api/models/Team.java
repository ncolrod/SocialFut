package ncolrod.socialfutv3.api.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    private int id;
    private String name;
    private String location;
    private String stadium;
    private String join_code;
    private String team_color;
    private String description;
    private User captain;
    private List<User> users;
}
