package ru.romanow.inst.services.common.utils

import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

object NetworkUtils {
    val hostName: String by lazy {
        try {
            val localhost = InetAddress.getLocalHost()
            localhost.hostName
        } catch (exception: UnknownHostException) {
            localAddressAsString
        }
    }

    private val localAddressAsString: String
        get() {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val addresses = interfaces.nextElement().inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (acceptableAddress(address)) {
                        return address.hostAddress
                    }
                }
            }
            return ""
        }

    private fun acceptableAddress(address: InetAddress?): Boolean {
        return address != null &&
            !address.isLoopbackAddress &&
            !address.isAnyLocalAddress &&
            !address.isLinkLocalAddress
    }
}