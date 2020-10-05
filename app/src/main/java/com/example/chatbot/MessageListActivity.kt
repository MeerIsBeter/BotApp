package com.example.chatbot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.R.attr.start
import androidx.recyclerview.widget.DiffUtil
import kotlin.concurrent.thread
import com.chaquo.python.PyObject
import com.chaquo.python.Python




class MessageListActivity : AppCompatActivity() {

    val messageList : ArrayList<Message> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

        val mMessageRecycler = findViewById<RecyclerView>(R.id.recyclerview_message_list)
        mMessageRecycler.layoutManager = LinearLayoutManager(this)
        val mMessageAdapter = MessageListAdapter(this, messageList)
        mMessageRecycler.adapter = mMessageAdapter

        val py = Python.getInstance()
        val mod = py.getModule("test")

        fun updateData() {
            mMessageAdapter.notifyItemInserted(messageList.size-1)
        }

        // handle sending and receiving messages
        val chatBox = findViewById<EditText>(R.id.edittext_chatbox)
        val sendButton = findViewById<Button>(R.id.button_chatbox_send)
        sendButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
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
                        Thread.sleep(500)
                        // send bot message
                        val botText = mod.callAttr("main", text).toString()
                        val isUser = false
                        val responseTime = System.currentTimeMillis()
                        val botMessage = Message(messageList.size-1, botText, isUser, responseTime)
                        messageList.add(botMessage)
                        runOnUiThread {
                            updateData()
                        }
                    }

                    mMessageAdapter.notifyDataSetChanged()

                }
            }
        })
    }

    class DiffUtilCallback(
        private val oldItems: List<Message>,
        private val newItems: List<Message>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // It works properly if Item is a data class
            // Otherwise, we should check if all fields of the items are the same
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size
    }
}
