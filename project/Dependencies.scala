import sbt.*

object Dependencies {

  private val doobieVersion = "1.0.0-RC12"

  val test: Seq[ModuleID] = Seq(
    "org.scalatestplus" %% "selenium-4-21"          % "3.2.19.0"    % Test,
    "org.slf4j"          % "slf4j-simple"           % "2.0.20"      % Test,
    "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.2.18"      % Test,
    "uk.gov.hmrc"       %% "ui-test-runner"         % "0.56.0"      % Test,
    "org.tpolecat"      %% "doobie-core"            % doobieVersion % Test,
    "org.tpolecat"      %% "doobie-postgres"        % doobieVersion % Test,
    "org.tpolecat"      %% "doobie-scalatest"       % doobieVersion % Test,
    "org.assertj"        % "assertj-core"           % "3.27.7"      % Test,
    "com.typesafe.play" %% "play-json"              % "2.10.8"      % Test
  )

}
