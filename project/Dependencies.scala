import sbt.*

object Dependencies {

  private val doobieVersion = "1.0.0-RC12"

  val test: Seq[ModuleID] = Seq(
    "org.seleniumhq.selenium" % "selenium-java"          % "4.41.0"      % Test,
    "com.vladsch.flexmark"    % "flexmark-all"           % "0.64.8"      % Test,
    "org.scalatest"          %% "scalatest"              % "3.2.19"      % Test,
    "org.scalatestplus"      %% "selenium-4-21"          % "3.2.19.0"    % Test,
    "org.slf4j"               % "slf4j-simple"           % "2.0.17"      % Test,
    "com.typesafe.play"      %% "play-ahc-ws-standalone" % "2.2.14"      % Test,
    "uk.gov.hmrc"            %% "ui-test-runner"         % "0.52.0"      % Test,
    "org.tpolecat"           %% "doobie-core"            % doobieVersion % Test,
    "org.tpolecat"           %% "doobie-postgres"        % doobieVersion % Test,
    "org.tpolecat"           %% "doobie-scalatest"       % doobieVersion % Test,
    "org.assertj"             % "assertj-core"           % "3.27.7"      % Test,
    "com.typesafe.play"      %% "play-json"              % "2.10.8"      % Test
  )

}
