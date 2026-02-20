import sbt.*

object Dependencies {

  private val doobieVersion = "1.0.0-RC11"

  val test: Seq[ModuleID] = Seq(
    "com.vladsch.flexmark" % "flexmark-all"           % "0.64.8"      % Test,
    "org.scalatest"       %% "scalatest"              % "3.2.19"      % Test,
    "org.scalatestplus"   %% "selenium-3-141"         % "3.2.10.0"    % Test,
    "org.slf4j"            % "slf4j-simple"           % "1.7.36"      % Test,
    "com.typesafe.play"   %% "play-ahc-ws-standalone" % "2.2.14"      % Test,
    "uk.gov.hmrc"         %% "ui-test-runner"         % "0.52.0"      % Test,
    "org.tpolecat"        %% "doobie-core"            % doobieVersion % Test,
    "org.tpolecat"        %% "doobie-postgres"        % doobieVersion % Test,
    "org.tpolecat"        %% "doobie-scalatest"       % doobieVersion % Test,
    "org.assertj"          % "assertj-core"           % "3.27.3"      % Test,
    "com.typesafe.play"   %% "play-json"              % "2.10.6"      % Test
  )

}
