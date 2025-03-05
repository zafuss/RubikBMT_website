package zafus.rubikbmt.rubikbmt_website.requestEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestUpdateStudent extends RequestUpdateUser{
    private int studiedCourse;
    private LocalDate birthday;
    private String parentName;
    private String parentPhoneNumber;
}
