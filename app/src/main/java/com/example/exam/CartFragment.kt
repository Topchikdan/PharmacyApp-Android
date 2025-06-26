package com.example.exam

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exam.databinding.FragmentCartBinding
import com.google.android.material.card.MaterialCardView

class CartFragment : Fragment(R.layout.fragment_cart) {

    private var _vb: FragmentCartBinding? = null
    private val vb get() = _vb!!
    private lateinit var vm: ShopVM
    private val adapter by lazy {
        CartAdapter(
            inc = { vm.inc(it) },
            dec = { vm.dec(it) },
            rem = { vm.rem(it) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _vb = FragmentCartBinding.bind(view)
        vm = (requireActivity() as MainActivity).shopVM

        vb.rvCart.layoutManager = LinearLayoutManager(requireContext())
        vb.rvCart.adapter = adapter

        vm.cart.observe(viewLifecycleOwner) { adapter.update(it) }
        vm.total.observe(viewLifecycleOwner) { vb.tvSum.text = "${it} ₽" }

        vb.btnCheckout.setOnClickListener {
            Toast.makeText(requireContext(), "⏳ Оформление заказа", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() { _vb = null; super.onDestroyView() }

    private class CartAdapter(
        val inc: (CartItem) -> Unit,
        val dec: (CartItem) -> Unit,
        val rem: (CartItem) -> Unit
    ) : RecyclerView.Adapter<CartAdapter.VH>() {

        private val items = mutableListOf<CartItem>()
        fun update(list: List<CartItem>) { items.apply { clear(); addAll(list) }; notifyDataSetChanged() }

        inner class VH(card: MaterialCardView) : RecyclerView.ViewHolder(card) {
            private val ctx = card.context

            private val name = TextView(ctx).apply {
                textSize = 19f
                setTypeface(null, Typeface.BOLD)
                setTextColor(0xFF181818.toInt())
                setLineSpacing(0f, 1.08f)
            }
            private val close = ImageButton(ctx).apply {
                setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                background = null
                setColorFilter(0xFFB0B0B0.toInt())
                layoutParams = LinearLayout.LayoutParams(48.dp, 48.dp)
                scaleType = ImageView.ScaleType.CENTER
                isFocusable = false
                isFocusableInTouchMode = false
                isClickable = true
            }
            private val price = TextView(ctx).apply {
                textSize = 18f
                setTypeface(null, Typeface.NORMAL)
                setTextColor(0xFF181818.toInt())
            }
            private val patient = TextView(ctx).apply {
                text = "1 пациент"
                textSize = 15f
                setTextColor(0xFF888888.toInt())
            }
            private fun grayBtn(iconRes: Int, iconColor: Int): ImageButton =
                ImageButton(ctx).apply {
                    setImageResource(iconRes)
                    background = GradientDrawable().apply {
                        setColor(Color.parseColor("#F4F4F6"))
                        cornerRadius = 12.dp.toFloat()
                    }
                    setColorFilter(iconColor)
                    scaleType = ImageView.ScaleType.CENTER
                    layoutParams = LinearLayout.LayoutParams(36.dp, 36.dp)
                    isFocusable = false
                    isFocusableInTouchMode = false
                    isClickable = true
                    setPadding(0, 0, 0, 0)
                }
            private val minus = grayBtn(R.drawable.ic_minus, 0xFFB0B0B0.toInt())
            private val plus  = grayBtn(R.drawable.ic_plus, 0xFF2176FF.toInt())

            private val qty = TextView(ctx).apply {
                textSize = 18f
                setTextColor(0xFF181818.toInt())
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                minWidth = 32.dp
                maxWidth = 32.dp
                gravity = Gravity.CENTER
            }

            private val controlsBox = LinearLayout(ctx).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(12.dp, 8.dp, 12.dp, 8.dp)
                background = GradientDrawable().apply {
                    setColor(Color.parseColor("#F4F4F6"))
                    cornerRadius = 12.dp.toFloat()
                }
                addView(minus)
                addView(qty, LinearLayout.LayoutParams(WRAP, 36.dp).apply { leftMargin = 2.dp; rightMargin = 2.dp })
                addView(plus)
            }

            init {
                card.radius = 18.dp.toFloat()
                card.cardElevation = 0f
                card.useCompatPadding = false
                card.setCardBackgroundColor(0xFFFFFFFF.toInt())
                card.strokeWidth = 1.dp
                card.strokeColor = Color.parseColor("#E6E6E6")
                card.layoutParams = ViewGroup.MarginLayoutParams(MATCH, WRAP).apply { bottomMargin = 16.dp }
                card.setContentPadding(0, 0, 0, 0)

                val top = LinearLayout(ctx).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    addView(name, LinearLayout.LayoutParams(0, WRAP, 1f))
                    addView(close, LinearLayout.LayoutParams(WRAP, WRAP))
                }
                val bottom = LinearLayout(ctx).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    setPadding(0, 16.dp, 0, 0)
                    addView(price, LinearLayout.LayoutParams(0, WRAP, 1f))
                    val patientControls = LinearLayout(ctx).apply {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.END or Gravity.CENTER_VERTICAL
                        addView(patient)
                        addView(controlsBox, LinearLayout.LayoutParams(WRAP, WRAP).apply { leftMargin = 16.dp })
                    }
                    addView(patientControls)
                }
                val root = LinearLayout(ctx).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(24.dp, 18.dp, 24.dp, 18.dp)
                    addView(top)
                    addView(bottom)
                }
                card.addView(root)
            }

            fun bind(ci: CartItem) {
                name.text = ci.service.name
                price.text = "${ci.service.price} ₽"
                qty.text = ci.qty.toString()
                plus.setOnClickListener { inc(ci) }
                minus.setOnClickListener { dec(ci) }
                close.setOnClickListener { rem(ci) }
            }
        }

        override fun onCreateViewHolder(p: ViewGroup, vt: Int) = VH(MaterialCardView(p.context))
        override fun onBindViewHolder(h: VH, pos: Int) = h.bind(items[pos])
        override fun getItemCount() = items.size
    }
}

private val Int.dp get() = (this * Resources.getSystem().displayMetrics.density).toInt()
private const val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
private const val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT
