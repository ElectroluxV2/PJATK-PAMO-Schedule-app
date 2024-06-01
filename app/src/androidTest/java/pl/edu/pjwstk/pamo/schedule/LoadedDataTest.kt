package pl.edu.pjwstk.pamo.schedule

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * [LoadedDataTest] verifies the functionality of the settings view and data loading process in the application.
 *
 * The test performs the following steps:
 * 1. Clicks on the settings icon in the bottom navigation bar to open the settings view.
 * 2. Sets the date to one day ahead of the current date in the `TextInputEditText`.
 * 3. Clicks on the "Reload data" button.
 * 4. Waits for 5 seconds to allow the data reload process to start.
 * 5. Verifies that the logs contain the message "Created scrapper".
 * 6. Waits for an additional 12 seconds for the data loading process to complete.
 * 7. Verifies that the logs contain the message "Done scrapping".
 * 8. Clicks on the "Today" icon in the bottom navigation bar.
 * 9. Verifies that the recycler view is displayed.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class LoadedDataTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

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
        textInputEditText.perform(replaceText(nextDate))


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
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
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
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
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
                withParent(withParent(withId(R.id.nav_host_fragment_activity_main))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))
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


