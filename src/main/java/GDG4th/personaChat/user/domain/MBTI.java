package GDG4th.personaChat.user.domain;

public enum MBTI {
    ISFP(0),
    ISFJ(1),
    ISTP(2),
    ISTJ(3),
    INFP(4),
    INFJ(5),
    INTP(6),
    INTJ(7),
    ESFP(8),
    ESFJ(9),
    ESTP(10),
    ESTJ(11),
    ENFP(12),
    ENFJ(13),
    ENTP(14),
    ENTJ(15);

    private final int number;

    MBTI(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}