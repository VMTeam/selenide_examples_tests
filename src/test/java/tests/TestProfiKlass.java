package tests;

import com.codeborne.selenide.SelenideElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.*;

public class TestProfiKlass {
    List<Integer> greenAnswers = new ArrayList<>();
    String questionText = "null";
    SelenideElement RESULT = $(byId("result-screen")).find(byId("res-wrongs"));

    @Test
    public void test() {
        openTicketByNumber(1);
        getGreenAnswer();
        repeatButtonClick();
        setGreenAnswers();
        Assert.assertEquals(RESULT.shouldBe(visible).getText(), "0");
    }

    private void getGreenAnswer() {
        for (SelenideElement ignored : $$(byClassName("quest-number")).filter(visible)) {
            updateQuestionText();
            for (int i = 0; i < $(byId("answers-block")).shouldBe(visible).findAll("div").size(); i++) {
                $(byId("answer" + i)).shouldBe(visible).click();
                if ($(byId("answer" + i)).getAttribute("class").contains("answer_green")) {
                    greenAnswers.add(i);
                    break;
                }
            }
        }
    }

    private void repeatButtonClick() {
        $(byId("result-screen")).shouldNotBe(visible).find(byId("but-repeat")).click();
    }

    private void setGreenAnswers() {
        for (int num : greenAnswers) {
            updateQuestionText();
            $(byId("answer" + num)).click();
        }
    }

    private void updateQuestionText() {
        // Тут далем проверку, чтобы предыдущий вопрос, не совпадал с текущим;
        // Нужна эта проверка чтобы дождаться/убедиться, что при переходе к следующиму вопрос сменился;
        $(byId("quest-block")).shouldNotBe(text(questionText));
        questionText = $(byId("quest-block")).getText();
    }

    private void openTicketByNumber(int number) {
        open("http://profiklass.kg/wp-content/t2019/" + number + ".html");
    }
}

