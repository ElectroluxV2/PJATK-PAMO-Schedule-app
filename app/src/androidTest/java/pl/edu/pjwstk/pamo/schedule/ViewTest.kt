package pl.edu.pjwstk.pamo.schedule

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
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
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * [ViewTest] verifies the functionality of various views in the application, such as the calendar view, settings, and data loading.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class ViewTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Verifies the functionality of the calendar view in the application.
     *
     * This test performs the following steps:
     * 1. Clicks on the calendar icon in the bottom navigation bar to open the calendar view.
     * 2. Selects the date "1" in the calendar.
     * 3. Checks if the text view displays a message containing "01", indicating that there is no data available for the selected date.
     */
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

    /**
     * Verifies the functionality of the reload button in the settings.
     *
     * This test performs the following steps:
     * 1. Clicks on the settings icon in the bottom navigation bar to open the settings view.
     * 2. Checks if the "Reload data" button is displayed.
     * 3. Clicks on the "Reload data" button.
     * 4. Checks if the logs text view is displayed.
     */
    @Test
    fun reloadTest() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_settings), withContentDescription("Settings"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val button = onView(
            allOf(
                withId(R.id.loadButton), withText("Reload data"),
                ViewMatchers.withParent(ViewMatchers.withParent(withId(R.id.nav_host_fragment_activity_main))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialButton = onView(
            allOf(
                withId(R.id.loadButton), withText("Reload data"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.logs),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
    }

    /**
     * Verifies that the data loading and scrapping functionality works correctly.
     *
     * This test performs the following steps:
     * 1. Clicks on the settings icon in the bottom navigation bar to open the settings view.
     * 2. Sets the date input to the next day.
     * 3. Clicks on the "Reload data" button.
     * 4. Checks if the logs text view displays the messages indicating the start and completion of data scrapping.
     * 5. Clicks on the "Today" icon in the bottom navigation bar.
     * 6. Checks if the recycler view is displayed.
     */
    @Test
    fun loadedDataTest() {
        // Click on the settings icon in the bottom navigation bar
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_settings), withContentDescription("Settings"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        // Get the current date
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val nextDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        // Click on the TextInputEditText and set the current date
        val textInputEditText = onView(
            allOf(
                withId(R.id.dateTo),
                childAtPosition(
                    childAtPosition(
                        withClassName(Matchers.`is`("com.google.android.material.textfield.TextInputLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(ViewActions.replaceText(nextDate))

        // Click on the "Reload data" button
        val materialButton = onView(
            allOf(
                withId(R.id.loadButton), withText("Reload data"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment_activity_main),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        SystemClock.sleep(5000)

        // Check if the text view displays a message containing "Created scrapper"
        val textView = onView(
            allOf(
                withId(R.id.logs),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText(Matchers.containsString("Created scrapper"))))

        // Wait for the "Done scrapping" message
        SystemClock.sleep(12000)

        // Check if the text view displays a message containing "Done scrapping"
        val textView2 = onView(
            allOf(
                withId(R.id.logs),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText(Matchers.containsString("Done scrapping"))))

        // Click on the "Today" icon in the bottom navigation bar
        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.navigation_today), withContentDescription("Today"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        // Check if the recycler view is displayed
        val recyclerView = onView(
            allOf(
                withId(R.id.subjects_recycler_view),
                ViewMatchers.withParent(ViewMatchers.withParent(withId(R.id.nav_host_fragment_activity_main))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))
    }

    /**
     * Returns a matcher that matches a child view at the given position within a parent view.
     *
     * @param parentMatcher the matcher of the parent view
     * @param position the position of the child view within the parent view
     * @return a matcher that matches the child view at the given position within the parent view
     */
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
