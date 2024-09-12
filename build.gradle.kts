plugins{
    alias(libs.plugins.kotlin)
    application
    alias(libs.plugins.serenity)
}

kotlin {
    jvmToolchain(17)

}
val serenity_version = "4.1.12"
val junit_platform_suite_version="1.11.0"
val cucumber_junit_platform_engine_version="7.18.1"
val logback_classic_version = "1.5.7"
val assertj_core_version = "3.26.3"

dependencies {
    testImplementation(libs.bundles.junit)
    testRuntimeOnly (libs.junit.engine)
    testImplementation ("net.serenity-bdd:serenity-core:${serenity_version}")
    testImplementation ("net.serenity-bdd:serenity-cucumber:${serenity_version}")
    testImplementation ("net.serenity-bdd:serenity-screenplay:${serenity_version}")
    testImplementation ("net.serenity-bdd:serenity-screenplay-webdriver:${serenity_version}")
    testImplementation ("net.serenity-bdd:serenity-ensure:${serenity_version}")

    testImplementation ("org.junit.platform:junit-platform-suite:${junit_platform_suite_version}")
    testCompileOnly("junit:junit:4.13.2")
    testImplementation ("io.cucumber:cucumber-junit-platform-engine:${cucumber_junit_platform_engine_version}")

    implementation ("ch.qos.logback:logback-classic:${logback_classic_version}")
    testImplementation ("org.assertj:assertj-core:${assertj_core_version}")
    implementation("org.casbin:jcasbin:1.55.0")

}

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy("aggregate")
}

