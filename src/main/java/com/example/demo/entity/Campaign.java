package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "title")
    @Length(min = 10, max = 50)
    @Pattern(regexp ="^[a-zA-Z0-9]+$")
    private String title;

    @Column(name = "detail")
    @Length(min = 20, max = 200)
    private String detail;

    @Column(name = "duplicate")
    private boolean duplicate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Category category;



    public enum Status {
        ACTIVE, WAITING_FOR_APPROVAL, DEACTIVATE;
    }

    public enum Category {
        TSS,OSS,OZEL_HAYAT_SIGORTASI,HAYAT_SIGORTASI,DIGER;
    }

}
