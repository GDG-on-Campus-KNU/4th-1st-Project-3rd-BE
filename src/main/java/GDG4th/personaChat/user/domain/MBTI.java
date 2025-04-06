package GDG4th.personaChat.user.domain;

public enum MBTI {
    ISTJ(0),
    ISFJ(1),
    INFJ(2),
    INTJ(3),
    ISTP(4),
    ISFP(5),
    INFP(6),
    INTP(7),
    ESTP(8),
    ESFP(9),
    ENFP(10),
    ENTP(11),
    ESTJ(12),
    ESFJ(13),
    ENFJ(14),
    ENTJ(15);

    private final int number;

    MBTI(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}