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
import kotlinx.android.synthetic.main.fragment_players.*
import ru.devsp.app.mtgcollections.R
import ru.devsp.app.mtgcollections.model.objects.Player
import ru.devsp.app.mtgcollections.view.components.NumberCounterView
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

            val healthCounter = view.findViewById<NumberCounterView>(R.id.healthCounter)
            val energyCounter = view.findViewById<NumberCounterView>(R.id.energyCounter)

            healthCounter.setOnCounterClickListener { inc ->
                if (inc) {
                    player.health++
                } else {
                    player.health--
                }
                updateCounterUi(healthCounter, energyCounter, player)
                savePlayer(task)
            }

            energyCounter.setOnCounterClickListener { inc ->
                if (inc) {
                    player.energy++
                } else if (player.energy > 0) {
                    player.energy--
                }
                updateCounterUi(healthCounter, energyCounter, player)
                savePlayer(task)
            }
            updateCounterUi(healthCounter, energyCounter, player)

            val parentBlock = getView()!!.findViewById<FrameLayout>(parent)
            parentBlock.addView(view)
        }
    }

    private fun updateCounterUi(health: NumberCounterView, energy: NumberCounterView, player: Player) {
        energy.setCount(String.format(Locale.getDefault(), "%d", player.energy))
        health.setCount(String.format(Locale.getDefault(), "%d", player.health))
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
