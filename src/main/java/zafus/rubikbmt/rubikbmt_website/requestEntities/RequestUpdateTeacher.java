package zafus.rubikbmt.rubikbmt_website.requestEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestUpdateTeacher extends RequestUpdateUser{
    private String description;
}
