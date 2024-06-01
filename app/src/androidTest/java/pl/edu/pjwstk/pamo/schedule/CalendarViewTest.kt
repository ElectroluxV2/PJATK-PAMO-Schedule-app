package pl.edu.pjwstk.pamo.schedule


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
/**
 * [CalendarViewTest] verifies the functionality of the calendar view in the application when there are no data entries.
 *
 * The test performs the following steps:
 * 1. Clicks on the calendar icon in the bottom navigation bar to open the calendar view.
 * 2. Selects the date "1" in the calendar.
 * 3. Checks if the text view displays a message containing "01", indicating that there is no data available for the selected date.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class CalendarViewTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun calendarViewTest() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_calendar), withContentDescription("Calendar"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val materialTextView = onView(
            allOf(
                withId(R.id.exTwoDayText), withText("1"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.navigation_calendar), withContentDescription("Calendar"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val materialTextView2 = onView(
            allOf(
                withId(R.id.exTwoDayText), withText("1"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialTextView2.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.noData),
                isDisplayed()
            )
        )
        textView.check(matches(withText(containsString("01")))) // Check if contains 01 after clicked 1 on calendar
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
}
