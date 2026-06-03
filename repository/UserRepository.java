package repository;

import model.User;

import java.util.List;

/**
 * 사용자 데이터를 관리하는 Repository
 */
public class UserRepository extends BaseRepository<User> {

    // 사용자 데이터 파일 경로
    private static final String FILE_PATH = "data/users.csv";

    /**
     * 생성자
     */
    public UserRepository() {
        super(FILE_PATH);
    }

    /**
     * 파일에서 사용자 데이터 로드
     *
     * CSV 형식:
     * studentId,name
     */
    @Override
    protected void loadFromFile() {

        for (String line : readLines()) {

            String[] parts = line.split(",");

            if (parts.length == 2) {

                String studentId = parts[0].trim();
                String name = parts[1].trim();

                dataList.add(
                        new User(studentId, name)
                );
            }
        }
    }

    /**
     * User 객체를 CSV 문자열로 변환
     */
    @Override
    protected String toCsvLine(User user) {
        return user.toCsvLine();
    }

    /**
     * 학번으로 사용자 조회
     */
    public synchronized User findByStudentId(String studentId) {

        List<User> result =
                findBy(user ->
                        user.getStudentId()
                                .equals(studentId));

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }
}