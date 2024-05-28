package pl.edu.pjwstk.pamo.schedule.ui.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjwstk.pamo.schedule.R
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject

/**
 * RecyclerView Adapter for displaying a list of PjatkSubject items.
 *
 * @param subjects The array of PjatkSubject items to display.
 */
class SubjectsAdapter(private val subjects: Array<PjatkSubject>) : RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    /**
     * ViewHolder class for the RecyclerView items.
     *
     * @param view The view associated with the ViewHolder.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val intervalText: TextView = view.findViewById(R.id.intervalText)
        val roomText: TextView = view.findViewById(R.id.roomText)
        val lecturersText: TextView = view.findViewById(R.id.lecturersText)
        val codesText: TextView = view.findViewById(R.id.codesText)
        val namesText: TextView = view.findViewById(R.id.namesText)
        val typeText: TextView = view.findViewById(R.id.typeText)
        val buildingText: TextView = view.findViewById(R.id.buildingText)
        val groupsText: TextView = view.findViewById(R.id.groupsText)
        val studentsCountText: TextView = view.findViewById(R.id.studentsCountText)
    }

    /**
     * Inflates the item layout and creates the ViewHolder.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.today_subject_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = subjects.size

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subject = subjects[position]

        holder.intervalText.text = subject.interval
        holder.roomText.text = subject.room
        holder.namesText.text = subject.names
        holder.lecturersText.text = subject.lecturers
        holder.codesText.text = subject.codes
        holder.typeText.text = subject.type
        holder.buildingText.text = subject.building
        holder.groupsText.text = subject.groups
        holder.studentsCountText.text = subject.studentsCount
    }
}
