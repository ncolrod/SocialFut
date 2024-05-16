package ncolrod.socialfutv3.api.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    private int id;

    private User assistant;

    private User scorerUser;

    private Match match;

}
