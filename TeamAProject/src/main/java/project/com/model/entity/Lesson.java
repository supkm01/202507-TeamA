package project.com.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "lesson", schema = "public")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "finish_time")
    private LocalTime finishTime;

    @Column(name = "lesson_name")
    private String lessonName;

    @Column(name = "lesson_detail")
    private String lessonDetail;

    @Column(name = "lesson_fee")
    private Integer lessonFee;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @Column(name = "admin_id")
    private Long adminId;
}
