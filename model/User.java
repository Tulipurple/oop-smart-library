package model;

/**
 * 사용자 정보 모델
 * 로그인 및 사용자 식별을 위한 학번과 이름만 관리한다.
 */
public class User {

    private final String studentId; // 학번 (고유 식별자)
    private final String name;      // 이름

    /**
     * 사용자 생성자
     *
     * @param studentId 학번
     * @param name 이름
     */
    public User(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    /**
     * 학번 반환
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * 이름 반환
     */
    public String getName() {
        return name;
    }

    /**
     * CSV 저장용 문자열 반환
     * 형식: 학번,이름
     */
    public String toCsvLine() {
        return studentId + "," + name;
    }

    @Override
    public String toString() {
        return "User{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}