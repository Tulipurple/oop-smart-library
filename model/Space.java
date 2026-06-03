package model;

/**
 * 공간(좌석, 스터디룸)의 공통 속성과 메서드를 정의하는 추상 클래스
 */
public abstract class Space {
    // 공통 속성
    private final String spaceId;
    private final String name;
    private final String location;

    // 생성자
    protected Space(String spaceId, String name, String location) {
        this.spaceId = spaceId;
        this.name = name;
        this.location = location;
    }

    // 공간 유형 반환 추상 메서드
    public abstract SpaceType getSpaceType();

    // CSV 라인 변환 추상 메서드
    public abstract String toCsvLine();

    // Getters
    public String getSpaceId() {
        return spaceId;
    }
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
}
