package com.atm.inet.entity;

import com.atm.inet.entity.auditing.Auditable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_file")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseFile extends Auditable<String> {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    private String name;
    private String contentType;
    private String path;
    private Long size;
}
