import org.testng.annotations.Test;
import utils.framework.AssinneeAnnotation;

public class MyTest {

    @AssinneeAnnotation(asigneeId = "5")
    @Test(groups = {"Regression", "HTTP"})
    public void commentCRUD() {
    }
    }

