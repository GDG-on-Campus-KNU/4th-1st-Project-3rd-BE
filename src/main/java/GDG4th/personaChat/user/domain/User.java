package GDG4th.personaChat.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일(구글에서 받아옴)
    @Column(name="email", unique = true, nullable = false)
    private String email;

    // 닉네임
    @Column(name = "nickname", unique = true)
    private String nickname;

    // 비밀번호
    @Column(name="password", nullable = false)
    private String password;

    // MBTI
    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(
        final String email,
        final String nickname,
        final String password,
        final MBTI mbti,
        final UserRole role
    ) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.mbti = mbti;
        this.role = role;
    }

}
