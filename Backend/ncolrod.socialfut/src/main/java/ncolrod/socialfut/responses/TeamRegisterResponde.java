package ncolrod.socialfut.responses;

import lombok.*;
import ncolrod.socialfut.entities.Team;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeamRegisterResponde {

    private String msg;
    private Team team;

}
