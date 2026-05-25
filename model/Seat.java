package model;

/**
 * 좌석 클래스
 */
public class Seat extends Space {
    // 좌석 고유 속성 - 성향테스트 결정 후 추가 예정

    // 생성자
    public Seat(String spaceId, String name, String location) {
        super(spaceId, name, location);
    }

    @Override
    public SpaceType getSpaceType() {
        return SpaceType.SEAT;
    }

    @Override
    public String toCsvLine() {
        // CSV 저장 형식: spacetype, spaceId,name,location,
        return String.format("%s,%s,%s,%s", getSpaceType(), getSpaceId(), getName(), getLocation());
    }

    // Getters for 좌석 고유 속성 - 성향테스트 결정 후 추가 예정
}