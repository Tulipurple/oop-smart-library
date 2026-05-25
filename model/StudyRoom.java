package model;

/**
 * 스터디룸 클래스
 * 
 */
public class StudyRoom extends Space {
    // 스터디룸 고유 속성
    private final int minPeople; // 최소 이용 인원
    private final int capacity; // 최대 수용 인원

    
    // 생성자
    public StudyRoom(String spaceId, String name, String location, int minPeople, int capacity) {
        super(spaceId, name, location);
        this.capacity = capacity;
        this.minPeople = minPeople;
    }

    @Override
    public SpaceType getSpaceType() {
        return SpaceType.STUDY_ROOM;
    }

    @Override
    public String toCsvLine() {
        // CSV 저장 형식: spacetype, spaceId,name,location,minPeople,capacity
        return String.format("%s,%s,%s,%s,%d,%d", getSpaceType(), getSpaceId(), getName(), getLocation(), minPeople, capacity);
    }

    // Getters
    public int getMinPeople() {
        return minPeople;
    }
    public int getCapacity() {
        return capacity;
    }
}
