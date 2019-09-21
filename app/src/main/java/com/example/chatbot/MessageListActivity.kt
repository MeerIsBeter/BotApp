package com.example.chatbot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessageListActivity : AppCompatActivity() {

    val messageList : ArrayList<Message> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)


        val message1 = Message("Hi there hdffd", true, System.currentTimeMillis())
        val message2 = Message("I AM BOT", false, System.currentTimeMillis())

        val mMessageRecycler = findViewById<RecyclerView>(R.id.recyclerview_message_list)
        mMessageRecycler.layoutManager = LinearLayoutManager(this)
        val mMessageAdapter = MessageListAdapter(this, messageList)
        mMessageRecycler.adapter = mMessageAdapter


        val chatBox = findViewById<EditText>(R.id.edittext_chatbox)
        val sendButton = findViewById<Button>(R.id.button_chatbox_send)
        sendButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                // Handler code here.
                val text = chatBox.text
                if (text.toString().isNotEmpty()) {
                    val user = true
                    val time = System.currentTimeMillis()
                    val message = Message(text.toString(), user, time)
                    messageList.add(message)
                    mMessageAdapter.notifyDataSetChanged()
                    chatBox.text.clear()
                }
            }
        })

    }

    fun updateData(newItems : List<Message>) {
        messageList.clear()
        messageList.addAll(newItems)
    }
}
