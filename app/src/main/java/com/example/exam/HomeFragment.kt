package com.example.exam

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

private val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
private val Float.dp: Float get() = (this * Resources.getSystem().displayMetrics.density)

class HomeFragment : Fragment() {

    private lateinit var shopVM: ShopVM
    private lateinit var recycler: RecyclerView

    private val customServices = listOf(
        Service(
            id = 1,
            name = "ПЦР-тест на определение РНК коронавируса стандартный",
            days = 2,
            price = 1800
        ),
        Service(
            id = 2,
            name = "Клинический анализ крови с лейкоцитарной формулировкой",
            days = 1,
            price = 690
        ),
        Service(
            id = 3,
            name = "Биохимический анализ крови, базовый",
            days = 1,
            price = 2440
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        recycler = root.findViewById(R.id.rvServices)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shopVM = (requireActivity() as MainActivity).shopVM
        val adapter = ServiceAdapter { shopVM.add(it) }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        adapter.submitList(customServices)
    }

    private class ServiceAdapter(
        val onAdd: (Service) -> Unit
    ) : ListAdapter<Service, ServiceAdapter.VH>(Diff) {

        inner class VH(card: MaterialCardView) : RecyclerView.ViewHolder(card) {
            private val ctx   = card.context
            private val name  = TextView(ctx).apply {
                textSize = 20f
                setTextColor(0xFF181818.toInt())
                setLineSpacing(0f, 1.08f)
                setPadding(0, 0, 0, 0)
            }
            private val days  = TextView(ctx).apply {
                textSize = 16f
                setTextColor(0xFF8B8B8B.toInt())
            }
            private val price = TextView(ctx).apply {
                textSize = 20f
                setTypeface(null,1)
                setTextColor(0xFF181818.toInt())
            }
            private val btn   = Button(ctx).apply {
                text = "Добавить"
                setTextColor(0xFFFFFFFF.toInt())
                textSize = 17f
                background = RoundedRectDrawable(0xFF2176FF.toInt(), 14.dp)
                setPadding(28.dp, 0, 28.dp, 0)
                isAllCaps = false
                setTypeface(null, Typeface.NORMAL)
            }

            init {
                val col = LinearLayout(ctx).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(28.dp, 24.dp, 28.dp, 24.dp)
                    addView(name, LinearLayout.LayoutParams(MATCH, WRAP))
                    addView(days, LinearLayout.LayoutParams(MATCH, WRAP).apply {
                        topMargin = 12.dp
                        bottomMargin = 0
                    })

                    val row = LinearLayout(ctx).apply {
                        orientation = LinearLayout.HORIZONTAL
                        gravity     = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                        setPadding(0, 16.dp, 0, 0)
                        addView(price, LinearLayout.LayoutParams(0, WRAP, 1f))
                        addView(btn, LinearLayout.LayoutParams(WRAP, 44.dp))
                    }
                    addView(row)
                }
                card.radius = 20.dp.toFloat()
                card.cardElevation = 2f.dp
                card.useCompatPadding = true
                card.setCardBackgroundColor(0xFFFFFFFF.toInt())
                card.layoutParams = ViewGroup.MarginLayoutParams(MATCH, WRAP).apply { bottomMargin = 16.dp }
                card.strokeColor = 0xFF2176FF.toInt()
                card.strokeWidth = 2.dp
                card.addView(col)
            }

            fun bind(s: Service) {
                name.text  = s.name
                days.text  = "${s.days} ${if (s.days == 1) "день" else "дня"}"
                price.text = "${s.price} ₽"
                btn.setOnClickListener { onAdd(s) }
            }
        }

        override fun onCreateViewHolder(p: ViewGroup, vt: Int) = VH(MaterialCardView(p.context))
        override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))

        private object Diff : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(o: Service, n: Service) = o.id == n.id
            override fun areContentsTheSame(o: Service, n: Service) = o == n
        }
    }

    companion object {
        private const val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
        private const val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}

class RoundedRectDrawable(private val color: Int, private val radius: Int): Drawable() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = this@RoundedRectDrawable.color }
    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(
            RectF(bounds),
            radius.toFloat(), radius.toFloat(),
            paint
        )
    }
    override fun setAlpha(alpha: Int) { paint.alpha = alpha }
    override fun setColorFilter(colorFilter: ColorFilter?) { paint.colorFilter = colorFilter }
    override fun getOpacity(): Int = PixelFormat.OPAQUE
}
