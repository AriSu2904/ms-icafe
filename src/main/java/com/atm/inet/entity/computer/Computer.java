package com.atm.inet.entity.computer;

import com.atm.inet.entity.auditing.Auditable;
import com.atm.inet.entity.constant.EStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_computer")
public class Computer extends Auditable<String> {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    private String name;

    @Column(name = "pc_code", unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @OneToOne(targetEntity = ComputerSpec.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "computer_spec_id")
    private ComputerSpec specification;

    @ManyToOne(targetEntity = Type.class)
    @JoinColumn(name = "type_id")
    private Type type;
}

