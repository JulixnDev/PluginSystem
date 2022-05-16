package dev.julixn.ps.api.license

import dev.julixn.ps.api.callbacks.PluginCallback
import dev.julixn.ps.api.config.PluginConfigManager
import dev.julixn.ps.api.logging.Logger

class LicenseChecker(private val configManager: PluginConfigManager, private val callback: PluginCallback, private val logger: Logger) {

    fun check(): Boolean {
        logger.log("Checking license...", "- LICENSE -")
        val licenseObject: LicenseConfigObject? = configManager.loadConfigObject("license", LicenseConfigObject::class.java) as LicenseConfigObject?

        if(callback.getLicenseController() == null) {
            logger.log("LicenseController is null! No license needed!", "- LICENSE -")
            return true
        }

        if(licenseObject == null) {
            configManager.saveConfigObject("license", LicenseConfigObject("YOUR_KEY"))
            logger.log("Please enter a license to use this plugin!", "- LICENSE -")
            callback.setValidLicense(false)
            return false
        } else if(!callback.getLicenseController()?.checkLicense(License(licenseObject.key))!!) {
            callback.setValidLicense(false)
            return false
        }

        callback.setValidLicense(true)
        logger.log("License is valid!", "- LICENSE -")
        return true
    }
}