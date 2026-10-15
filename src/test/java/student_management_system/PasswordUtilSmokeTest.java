package student_management_system;

/**
 * Dependency-free smoke tests so password handling can be verified in CI
 * without adding a test framework to this Eclipse Dynamic Web Project.
 */
public final class PasswordUtilSmokeTest {
    public static void main(String[] args) {
        String password = "Correct-Horse-42!";
        String firstHash = PasswordUtil.hash(password);
        String secondHash = PasswordUtil.hash(password);

        require(PasswordUtil.isHashed(firstHash), "hash format should be recognized");
        require(!firstHash.equals(password), "stored password must not be plain text");
        require(PasswordUtil.verify(password, firstHash), "correct password should verify");
        require(!PasswordUtil.verify("wrong-password", firstHash), "wrong password must fail");
        require(!firstHash.equals(secondHash), "each hash should use a fresh random salt");
        require(PasswordUtil.verify(password, password), "legacy password should verify for migration");
        require(!PasswordUtil.verify("wrong-password", password), "wrong legacy password must fail");

        System.out.println("PASS: password hashing, salt randomness, verification, and legacy verification");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
