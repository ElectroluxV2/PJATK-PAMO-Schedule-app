package pl.edu.pjwstk.pamo.schedule.ui.today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.edu.pjwstk.pamo.schedule.R
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject

class SubjectsAdapter(private val subjects: Array<PjatkSubject>) : RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val intervalText: TextView
        val roomText: TextView
        val lecturersText: TextView
        val codesText: TextView
        val namesText: TextView
        val typeText: TextView
        val buildingText: TextView
        val groupsText: TextView
        val studentsCountText: TextView

        init {
            intervalText = view.findViewById(R.id.intervalText)
            roomText = view.findViewById(R.id.roomText)
            namesText = view.findViewById(R.id.namesText)
            lecturersText = view.findViewById(R.id.lecturersText)
            codesText = view.findViewById(R.id.codesText)
            typeText = view.findViewById(R.id.typeText)
            buildingText = view.findViewById(R.id.buildingText)
            groupsText = view.findViewById(R.id.groupsText)
            studentsCountText = view.findViewById(R.id.studentsCountText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.today_subject_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = subjects.size

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