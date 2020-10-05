package com.mdvmeijer.chitchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlin.concurrent.thread
import com.chaquo.python.Python


class MessageListActivity : AppCompatActivity() {

    val messageList: ArrayList<Message> = arrayListOf()
    val mMessageAdapter: MessageListAdapter = MessageListAdapter(this, messageList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

        val mMessageRecycler = findViewById<RecyclerView>(R.id.recyclerview_message_list)
        mMessageRecycler.layoutManager = LinearLayoutManager(this)
        mMessageRecycler.adapter = mMessageAdapter

        val py = Python.getInstance()
        val mod = py.getModule("test")

        fun updateData() {
            mMessageAdapter.notifyItemInserted(messageList.size-1)
        }

        fun sendBotMessage(isResponding: Boolean, text: String) {
            Thread.sleep(500)

            val botText: String = if (isResponding) {
                // call the Python chatbot module to construct an answer
                mod.callAttr("main", text).toString()
            } else {
                // use whatever string was passed as argument
                text
            }

            val isUser = false
            val responseTime = System.currentTimeMillis()
            val botMessage = Message(messageList.size-1, botText, isUser, responseTime)
            messageList.add(botMessage)
            runOnUiThread {
                updateData()
            }
        }

        sendBotMessage(false, "Hi there, welcome to this chatroom! I am a bot-in-training and I would love to talk with you, just send me a message whenever you want :)")

        // handle sending and receiving messages
        val chatBox = findViewById<EditText>(R.id.edittext_chatbox)
        val sendButton = findViewById<Button>(R.id.button_chatbox_send)
        sendButton.setOnClickListener {
            // Handler code here.
            val text = chatBox.text.toString()
            if (text.isNotEmpty()) {
                // send user message
                val user = true
                val sendTime = System.currentTimeMillis()
                val message = Message(messageList.size-1, text, user, sendTime)
                messageList.add(message)
                updateData()
                chatBox.text.clear()

                thread(start=true) {
                    sendBotMessage(true, text)
                }
            }
        }
    }

    fun updateData() {
        mMessageAdapter.notifyItemInserted(messageList.size-1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu items for use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear_logs -> {
                messageList.clear()
                mMessageAdapter.notifyDataSetChanged()
            }
        }
        return true
    }
}
