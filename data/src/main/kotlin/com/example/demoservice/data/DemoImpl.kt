package com.example.demoservice.data


import com.example.demoservice.core.demoGateways.Demo
import org.springframework.data.annotation.Id
import java.util.*


data class DemoImpl(@Id override val id : String = UUID.randomUUID().toString(), override val test : String) : Demo