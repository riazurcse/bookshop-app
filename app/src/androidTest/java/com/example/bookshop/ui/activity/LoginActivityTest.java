package com.example.bookshop.ui.activity;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.util.Patterns;

import com.example.bookshop.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testEmptyPasswordField() {
        String email = "test@email.com";
        String password = "";
        String displayedMessage = "Enter a Password";
        Espresso.onView(withId(R.id.usernameET)).perform(typeText(email));
        Espresso.onView(withId(R.id.passwordET)).perform(typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btnLogin)).perform(click());
        ViewInteraction passwordField = Espresso.onView(withId(R.id.passwordET));
        passwordField.check(matches(hasErrorText(displayedMessage)));
    }

    @Test
    public void testEmptyEmailField() {
        String email = "";
        String password = "123654";
        String displayedMessage = "Enter an E-Mail Address";
        Espresso.onView(withId(R.id.usernameET)).perform(typeText(email));
        Espresso.onView(withId(R.id.passwordET)).perform(typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btnLogin)).perform(click());
        ViewInteraction emailField = Espresso.onView(withId(R.id.usernameET));
        emailField.check(matches(hasErrorText(displayedMessage)));
    }

    @Test
    public void testEmailValidation() {
        String email = "riajgmail.com";
        String password = "123654";
        String displayedMessage = "Enter a Valid E-mail Address";
        Espresso.onView(withId(R.id.usernameET)).perform(typeText(email));
        Espresso.onView(withId(R.id.passwordET)).perform(typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btnLogin)).perform(click());
        ViewInteraction emailField = Espresso.onView(withId(R.id.usernameET));
        emailField.check(matches(hasErrorText(displayedMessage)));
    }

    @Test
    public void testPasswordLengthValidation() {
        String email = "riaj@gmail.com";
        String password = "12354";
        String displayedMessage = "Enter at least 6 digit password";
        Espresso.onView(withId(R.id.usernameET)).perform(typeText(email));
        Espresso.onView(withId(R.id.passwordET)).perform(typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.btnLogin)).perform(click());
        ViewInteraction passwordField = Espresso.onView(withId(R.id.passwordET));
        passwordField.check(matches(hasErrorText(displayedMessage)));
    }

    @After
    public void tearDown() throws Exception {
    }
}