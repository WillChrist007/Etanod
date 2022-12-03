package com.william.etanodv2


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerActivityTest() {
        onView(isRoot()).perform(waitFor(3000))
        val materialButton = onView(
            allOf(
                withId(R.id.btnRegistration), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.LayoutRegistrasi),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    3
                )
            )
        )
        materialButton.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.et_username),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.username),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), click())
        textInputEditText.perform(scrollTo(), replaceText("Kelompok 5"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnRegistration), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.LayoutRegistrasi),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.password),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText("admin"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btnRegistration), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.LayoutRegistrasi),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    3
                ),
            )
        )
        materialButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.et_email),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.email),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText3.perform(scrollTo(), replaceText("kelompok5@gmail.com"), closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btnRegistration), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.LayoutRegistrasi),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.etTanggal),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tanggal_lahir),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText4.perform(scrollTo(), replaceText("01/02/2023"), closeSoftKeyboard())

        val materialButton5 = onView(
            allOf(
                withId(R.id.btnRegistration), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.LayoutRegistrasi),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.et_telepon),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.telepon),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText5.perform(scrollTo(), replaceText("123456789012"), closeSoftKeyboard())

        val materialButton6 = onView(
            allOf(
                withId(R.id.btnRegistration), withText("Register"),
                childAtPosition(
                    allOf(
                        withId(R.id.LayoutRegistrasi),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())
        onView(isRoot()).perform(waitFor(3000))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    private fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $delay milliseconds."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}
