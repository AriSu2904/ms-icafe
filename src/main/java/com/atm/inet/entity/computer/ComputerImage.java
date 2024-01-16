package com.atm.inet.entity.computer;

import com.atm.inet.entity.BaseFile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_computer_image")
@SuperBuilder
@IdClass(String.class)
public class ComputerImage extends BaseFile {


    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @JsonBackReference
    private Type type;
}
