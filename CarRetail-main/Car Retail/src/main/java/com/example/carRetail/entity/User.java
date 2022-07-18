package com.example.carRetail.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String mobileNo;
    private String address;
    private String password;
    @Column(columnDefinition = "boolean default false")
    private boolean deleteStatus;
    @OneToOne
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private Role role;

    public User(String firstName, String password, Role role){
        this.firstName = firstName;
        this.password = password;
        this.role = role;
    }
}
