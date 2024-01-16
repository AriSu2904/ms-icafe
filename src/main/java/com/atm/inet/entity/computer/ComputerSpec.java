package com.atm.inet.entity.computer;

import com.atm.inet.entity.auditing.Auditable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "m_computer_spec")
public class ComputerSpec extends Auditable<String> {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    private String processor;
    private String ram;
    private String monitor;
    private String ssd;
    private String vga;



}
