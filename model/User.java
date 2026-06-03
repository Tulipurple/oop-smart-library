package model;

/**
 * 사용자 정보를 저장하는 모델 클래스
 */
public class User {

    // 학번 (사용자 고유 식별자)
    private final String studentId;

    // 사용자 이름
    private final String name;

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
     *
     * @return 학번
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * 이름 반환
     *
     * @return 이름
     */
    public String getName() {
        return name;
    }

    /**
     * CSV 저장용 문자열 반환
     * 형식 : studentId,name
     *
     * @return CSV 형식 문자열
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