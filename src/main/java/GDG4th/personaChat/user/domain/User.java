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

    @Column(name="openedMbti", nullable = false)
    private int openedMbti;

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
        this.openedMbti = 0;
    }

    public boolean openMbti(String mbti) {
        if(isOpened(mbti)) {
            this.openedMbti = openedMbti | (1 << MBTI.valueOf(mbti).getNumber());
            return true;
        }
        return false;
    }

    public boolean closeMbti(String mbti) {
        if(isOpened(mbti)) {
            this.openedMbti = openedMbti & ~(1 << MBTI.valueOf(mbti).getNumber());
            return true;
        }
        return false;
    }

    private boolean isOpened(String mbti) {
        return (this.openedMbti & (1 << MBTI.valueOf(mbti).getNumber())) != 0;
    }
}
