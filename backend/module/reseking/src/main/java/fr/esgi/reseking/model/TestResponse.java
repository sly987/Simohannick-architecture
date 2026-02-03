package fr.esgi.reseking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "response")
@NoArgsConstructor
public class TestResponse {
    @Id
    private Integer id;
    private String value;
}
