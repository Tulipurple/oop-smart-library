package repository;

import model.Space;
import model.Seat;
import model.StudyRoom;
import model.SpaceType;

import java.util.List;

/**
 * 공간(Seat, StudyRoom) 데이터를 파일로부터 관리하는 Repository
 */
public class SpaceRepository extends BaseRepository<Space> {
    private static final String FILE_PATH = "data/spaces.csv";

    public SpaceRepository() {
        super(FILE_PATH);
    }

    // ───────────────────────────────────────────────────────
    // 파일 I/O
    // ───────────────────────────────────────────────────────
    @Override
    protected void loadFromFile(){
        for (String line : readLines()) {
            try {
                Space space = parseLine(line);
                if (space!=null){
                    dataList.add(space);
                }
            } catch (Exception e) {
                System.err.println("공간 데이터 파싱 오류: " + e.getMessage());
            }
        }
    }
    
    @Override
    protected String toCsvLine(Space space){
        return space.toCsvLine();
    }

    private Space parseLine(String line) throws Exception {
        String[] parts = line.split(",");
        String type = parts[0].trim();

        if ("SEAT".equals(type)) {
            if (parts.length < 13) {
                throw new Exception("잘못된 Seat 데이터 형식: " + line);
            }
            return new Seat(
                    parts[1],                       // spaceId
                    parts[2],                       // name
                    parts[3],                       // location
                    Integer.parseInt(parts[4]),     // quietLevel
                    Integer.parseInt(parts[5]),     // conversationLevel
                    Integer.parseInt(parts[6]),     // openLevel
                    Integer.parseInt(parts[7]),     // partitionLevel
                    Integer.parseInt(parts[8]),     // typingLevel
                    Integer.parseInt(parts[9]),     // socketLevel
                    Integer.parseInt(parts[10]),    // sofaLevel
                    Integer.parseInt(parts[11]),    // rollingChairLevel
                    Integer.parseInt(parts[12])     // normalChairLevel
            );
        }

        if ("STUDY_ROOM".equals(type)) {
            if (parts.length < 6) {
                throw new Exception("잘못된 StudyRoom 데이터 형식: " + line);
            }
            return new StudyRoom(
                    parts[1],                       // spaceId
                    parts[2],                       // name
                    parts[3],                       // location
                    Integer.parseInt(parts[4]),     // minPeople
                    Integer.parseInt(parts[5])      // capacity
            );
        }

        throw new Exception("알 수 없는 공간 유형: " + type);
    }

    // ───────────────────────────────────────────────────────
    // 조회 메서드
    // ───────────────────────────────────────────────────────
    
    // spaceId로 공간 단건 조회
    public Space findById(String spaceId) {
        List<Space> result = findBy(s -> s.getSpaceId().equals(spaceId));
        if (result.isEmpty()) return null;
        return result.get(0);
    }
    // SEAT 타입 공간 전체 조회
    public List<Space> findAllSeats() {
        return findBy(s -> s.getSpaceType() == SpaceType.SEAT);
    }

    // STUDY_ROOM 타입 공간 전체 조회
    public List<Space> findAllStudyRooms() {
        return findBy(s -> s.getSpaceType() == SpaceType.STUDY_ROOM);
    }
}