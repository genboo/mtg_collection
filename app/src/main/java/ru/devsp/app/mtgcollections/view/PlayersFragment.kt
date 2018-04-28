package ru.devsp.app.mtgcollections.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_players.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Player
import ru.devsp.app.mtgcollections.viewmodel.GameViewModel
import java.util.*
import javax.inject.Inject


/**
 * Игроки
 * Created by gen on 03.10.2017.
 */

class PlayersFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var handler: Handler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_players, container, false)
        initFragment()
        handler = Handler()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbar()

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

        viewModel.getPlayer(1).observe(this, Observer { player ->
            if (player == null) {
                viewModel.save(getDefaultPlayerObject(1))
            } else {
                viewModel.getPlayer(1).removeObservers(this)
                initPlayerLayer(viewModel, player, R.id.fl_player_one)
            }
        })

        viewModel.getPlayer(2).observe(this, Observer { player ->
            if (player == null) {
                viewModel.save(getDefaultPlayerObject(2))
            } else {
                viewModel.getPlayer(2).removeObservers(this)
                initPlayerLayer(viewModel, player, R.id.fl_player_two)
            }
        })
    }

    private fun initPlayerLayer(viewModel: GameViewModel, player: Player, parent: Int) {
        val view = layoutInflater.inflate(R.layout.layout_player, mainBlock, false)
        if (view != null && getView() != null) {

            val task = Runnable { viewModel.save(player) }

            val health = view.findViewById<TextView>(R.id.tv_player_health_count)
            val energy = view.findViewById<TextView>(R.id.tv_player_energy_count)

            updateCounterUi(health, energy, player)

            val healthMinus = view.findViewById<ImageButton>(R.id.ib_player_health_minus)
            healthMinus.setOnClickListener { _ ->
                player.health--
                updateCounterUi(health, energy, player)
                savePlayer(task)
            }

            val healthPlus = view.findViewById<ImageButton>(R.id.ib_player_health_plus)
            healthPlus.setOnClickListener { _ ->
                player.health++
                updateCounterUi(health, energy, player)
                savePlayer(task)
            }

            val energyMinus = view.findViewById<ImageButton>(R.id.ib_player_energy_minus)
            energyMinus.setOnClickListener { _ ->
                if (player.energy > 0) {
                    player.energy--
                    updateCounterUi(health, energy, player)
                    savePlayer(task)
                }
            }

            val energyPlus = view.findViewById<ImageButton>(R.id.ib_player_energy_plus)
            energyPlus.setOnClickListener { _ ->
                player.energy++
                updateCounterUi(health, energy, player)
                savePlayer(task)
            }
            val parentBlock = getView()!!.findViewById<FrameLayout>(parent)
            parentBlock.addView(view)
        }
    }

    private fun updateCounterUi(health: TextView, energy: TextView, player: Player) {
        energy.text = String.format(Locale.getDefault(), "%d", player.energy)
        health.text = String.format(Locale.getDefault(), "%d", player.health)
    }

    private fun savePlayer(task: Runnable) {
        mainBlock.removeCallbacks(task)
        mainBlock.postDelayed(task, 1000)
    }

    private fun getDefaultPlayerObject(id: Int): Player {
        val item = Player()
        item.id = id.toLong()
        item.health = 20
        return item
    }

    override fun inject() {
        component?.inject(this)
    }

    override fun getTitle(): String {
        return "Игроки"
    }

}
