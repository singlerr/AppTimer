/*
Copyright 2022 singlerr

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

   * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
   * Neither the name of singlerr nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package kr.apptimer.android.utils;

import android.content.Context;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import kr.apptimer.utils.Consumer;
import kr.apptimer.utils.Predicate;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/***
 * Utility class for requesting android permission by pipeline
 * Easily request permission with {@link PermissionRequest#builder()}
 * @deprecated Do not use. Just leave for legacy. It does not work properly, use Dexter library instead
 * @author Singlerr
 */
@Deprecated()
@Builder
public final class PermissionRequest {

  @Singular("withPermission")
  private List<Permission> pipeline;

  private final AppCompatActivity context;

  /***
   * Start requesting permissions in order.
   */
  public void execute() {
    for (Permission permission : pipeline) {
      // Works only passes condition
      if (permission.getCondition().test(context)) {
        Consumer<Context> alternativeExecutor = permission.getExecutor();
        // When executor is null, then request permission in my way.
        if (alternativeExecutor == null) {
          requestPermission(permission);
        } else {
          // Do user-defined
          alternativeExecutor.accept(context);
        }
      }
    }
  }

  private void requestPermission(Permission permission) {
    ActivityResultLauncher<String> launcher =
        context.registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
              if (isGranted) {
                if (permission.onSuccess != null) permission.onSuccess.run();
              } else {
                if (permission.onFailed != null) permission.onFailed.run();
              }
            });
    launcher.launch(permission.action);
  }

  /***
   * Subclass of {@link PermissionRequest} which contains condition of request of permission or permission invoker
   * @author Singlerr
   */
  @Getter
  public static class Permission {

    private String action;

    private Predicate<Context> condition;

    @Nullable private Consumer<Context> executor;

    private Runnable onSuccess;

    private Runnable onFailed;

    /***
     * Creates information about permission
     * @param permission String id of permission or action
     * @param when decides whether request permission or not
     * @param request {@link PermissionRequest} requests permission with suggested procedure
     *                                         @see <a href="https://developer.android.com/training/permissions/requesting?hl=ko"></a>
     * @param onSuccess callback when permission request is success
     * @param onFailed callback when permission request is failed
     */
    @Builder
    public Permission(
        String permission,
        Predicate<Context> when,
        @Nullable Consumer<Context> request,
        Runnable onSuccess,
        Runnable onFailed) {
      this.action = permission;
      this.condition = when;
      this.executor = request;
      this.onSuccess = onSuccess;
      this.onFailed = onFailed;
    }
  }
}
